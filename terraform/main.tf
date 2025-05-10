terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.16"
    }
  }

  required_version = ">= 1.2.0"
}

provider "aws" {
  region = var.region
}

resource "aws_s3_bucket" "code_connect_s3" {
  bucket = "code-connect-s3-v2"
  acl    = "private"
}

resource "aws_s3_object" "docker_compose" {
  bucket = aws_s3_bucket.code_connect_s3.bucket
  key    = "docker-compose.yml"
  source = var.s3_docker_compose_source
  acl    = "private"
}

resource "aws_iam_role" "ec2_s3_access_role" {
  name = "EC2S3AccessRole"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ec2.amazonaws.com"
        }
      },
    ]
  })
}

resource "aws_iam_role_policy_attachment" "attach_s3_read_only_policy" {
  role       = aws_iam_role.ec2_s3_access_role.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonS3ReadOnlyAccess"
}

resource "aws_iam_instance_profile" "ec2_s3_access_profile" {
  name = "ec2-s3-access-profile"
  role = aws_iam_role.ec2_s3_access_role.name
}

resource "aws_security_group" "app_server_security_group" {
  name        = "app-server-security-group"
  description = "Allow SSH, HTTP, and 8080 traffic"
  tags = {
    Name = "terraform-ec2-server-security-group"
  }

  ingress {
    from_port = 22
    to_port   = 22
    protocol  = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port = 80
    to_port   = 80
    protocol  = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port = 8080
    to_port   = 8080
    protocol  = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port = 3000
    to_port   = 3000
    protocol  = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_instance" "app_server" {
  ami                  = var.iam_image
  instance_type        = var.instance_type
  key_name             = var.key_pair
  security_groups = [aws_security_group.app_server_security_group.name]
  iam_instance_profile = aws_iam_instance_profile.ec2_s3_access_profile.name
  root_block_device {
    volume_size = 15
    volume_type = "gp3"
  }

  user_data = <<-EOF
#!/bin/bash
sudo yum update -y
sudo yum install -y docker
sudo service docker start
sudo usermod -a -G docker ec2-user

sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
docker-compose --version

echo "Starting Docker Compose..." >> /var/log/user-data.log
aws s3 cp s3://code-connect-s3-v2/docker-compose.yml /home/ec2-user/docker-compose.yml >> /var/log/user-data.log 2>&1
cd /home/ec2-user

while ! sudo systemctl is-active --quiet docker; do
  echo "Waiting for Docker to start..." >> /var/log/user-data.log
  sleep 20
done

sudo docker-compose up -d >> /var/log/user-data.log 2>&1
echo "Docker Compose finished running." >> /var/log/user-data.log

EOF

  tags = {
    Name = "terraform-base-main-ec2-server"
  }
}

resource "aws_lb_target_group_attachment" "lb_attachment" {
  target_group_arn = var.alb_target_group_arn
  target_id        = aws_instance.app_server.id
  port             = 3000
}

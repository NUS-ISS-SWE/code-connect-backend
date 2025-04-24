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
  region = "ap-southeast-1"
}

resource "aws_security_group" "app_server_security_group" {
  name        = "app-server-security-group"
  description = "Allow SSH, HTTP and 8080 traffic"
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

  egress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_instance" "app_server" {
  ami           = "ami-05ab12222a9f39021"
  instance_type = "t2.micro"
  key_name      = "code-connect-key-pair"
  security_groups = [aws_security_group.app_server_security_group.name]
  root_block_device {
    volume_size = 15
    volume_type = "gp3"
  }
  tags = {
    Name = "terraform-base-main-ec2-server"
  }
}
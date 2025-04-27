variable "subnet_ids" {
  description = "List of subnet IDs for the ALB"
  type = list(string)
  default = ["subnet-0388e7b3c238874d8", "subnet-03f4377e7af006ecb", "subnet-007f0fccf788b15ee"]
}

variable "vpc_id" {
  description = "The VPC ID where the ALB and EC2 instances are located"
  type        = string
  default     = "vpc-0cea0fd7b3c0a6ff8"
}

variable "region" {
  description = "The AWS region where resources are deployed"
  type        = string
  default     = "ap-southeast-1"
}

variable "instance_type" {
  description = "The type of EC2 instance"
  type        = string
  default     = "t2.large"
}

variable "iam_image" {
    description = "The AMI ID for the EC2 instance"
    type        = string
    default     = "ami-05ab12222a9f39021"
}
variable "key_pair" {
    description = "The name of the key pair to use for SSH access"
    type        = string
    default     = "code-connect-key-pair"
}

variable "s3_docker_compose_source" {
  description = "The path to the private key file for SSH access"
  type        = string
  default     = "C:\\Users\\mingw\\IdeaProjects\\nus\\code-connect-backend\\docker-compose.yml"
}

variable "alb_target_group_arn" {
  description = "The ARN of the target group for the ALB"
  type        = string
  default     = "arn:aws:elasticloadbalancing:ap-southeast-1:697982515707:targetgroup/mw-target-group/caef3c2f16c6112c"
}
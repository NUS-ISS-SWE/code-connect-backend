variable "subnet_ids" {
  description = "List of subnet IDs for the ALB"
  type = list(string)
  default = ["subnet-0c80e6a2c951f19ad", "subnet-0ada350d55d95b725", "subnet-09cfd5ea358569994"]
}

variable "vpc_id" {
  description = "The VPC ID where the ALB and EC2 instances are located"
  type        = string
  default     = "vpc-0f8e3dffaac6b9bf5"
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
    default     = "jenkins_kp"
}

variable "s3_docker_compose_source" {
  description = "The path to the private key file for SSH access"
  type        = string
  default     = "../docker-compose.yml"
}

variable "alb_target_group_arn" {
  description = "The ARN of the target group for the ALB"
  type        = string
  default     = "arn:aws:elasticloadbalancing:ap-southeast-1:221082174830:targetgroup/CodeConnectTargetGroup/5fe342ba07d17458"
}
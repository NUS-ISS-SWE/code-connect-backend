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

resource "aws_instance" "app_server" {
  ami           = "ami-05ab12222a9f39021"
  instance_type = "t2.micro"

  tags = {
    Name = "base-main-ec2-server"
  }
}
variable "service_name" {
  type = string
}

variable "region" {
  type = string
}

variable "environments" {
  type = map(object({
    aws_lb_name       = string
  }))
}

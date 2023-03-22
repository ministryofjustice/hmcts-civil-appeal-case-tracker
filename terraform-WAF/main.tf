provider "aws" {
  region = var.region
}

module "awsWaf" {
  source = "./modules/awsWaf"

  for_each = var.environments

  aws_lb_name                   = each.value.aws_lb_name
  aws_wafv2_web_acl_name        = "tf-${each.key}-${var.service_name}-webacli"
  aws_wafv2_web_acl_description = "Terraform web acl for ${var.service_name} ${each.key}"
  aws_cloudwatch_log_group_name = "aws-waf-logs-${var.service_name}-${each.key}"
}

data "aws_secretsmanager_secret_version" "webhook_url" {
  secret_id = "casetracker_waf_notification_slack_webhook_url"
}

locals {
  casetracker_waf_notification_slack_webhook_url = jsondecode(
    data.aws_secretsmanager_secret_version.webhook_url.secret_string
  )
}

module "awsWaf_slack_notifications" {
  source = "./modules/slack_notifications"

  for_each = var.environments

  slack_webhook_url             = local.casetracker_waf_notification_slack_webhook_url["${each.key}"]
  aws_cloudwatch_log_group_name = "aws-waf-logs-${var.service_name}-${each.key}"
  waf_log_group_arn             = module.awsWaf["${each.key}"].waf_log_group_arn
  environment                   = each.key
  service_name                  = var.service_name
}

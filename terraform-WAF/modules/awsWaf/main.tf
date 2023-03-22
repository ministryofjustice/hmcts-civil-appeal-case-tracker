data "aws_lb" "aws-application-lb" {
  name = var.aws_lb_name
}

resource "aws_wafv2_web_acl" "webacl1" {
  name        = var.aws_wafv2_web_acl_name
  description = var.aws_wafv2_web_acl_description
  scope       = "REGIONAL"

  default_action {
    allow {}
  }

  #----------------------- Rule: Common Rule Set -------------------------
  rule {
    name     = "AWSManagedRulesCommonRuleSet"
    priority = 0

    override_action {
      none {}
    }

    statement {
      managed_rule_group_statement {
        name        = "AWSManagedRulesCommonRuleSet"
        vendor_name = "AWS"

        rule_action_override {
          action_to_use {
            allow {}
          }

          name = "CrossSiteScripting_COOKIE"
        }
      }
    }

    visibility_config {
      cloudwatch_metrics_enabled = true
      metric_name                = "AWSManagedRulesCommonRuleSetMetric"
      sampled_requests_enabled   = true
    }
  }
  #----------------------- Rule: SQLi Rule Set -------------------------

  rule {
    name     = "AWSManagedRulesSQLiRuleSet"
    priority = 1

    override_action {
      none {}
    }

    statement {
      managed_rule_group_statement {
        name        = "AWSManagedRulesSQLiRuleSet"
        vendor_name = "AWS"
      }
    }

    visibility_config {
      cloudwatch_metrics_enabled = true
      metric_name                = "AWSManagedRulesSQLiRuleSetMetric"
      sampled_requests_enabled   = true
    }
  }
  #----------------------- Rule: Linux Rule Set -------------------------
  rule {
    name     = "AWSManagedRulesLinuxRuleSet"
    priority = 2

    override_action {
      none {}
    }

    statement {
      managed_rule_group_statement {
        name        = "AWSManagedRulesLinuxRuleSet"
        vendor_name = "AWS"
      }
    }

    visibility_config {
      cloudwatch_metrics_enabled = true
      metric_name                = "AWSManagedRulesLinuxRuleSet"
      sampled_requests_enabled   = true
    }
  }
  #----------------------- Rule: Known Bad Inputs Rule Set -------------------------

  rule {
    name     = "AWSManagedRulesKnownBadInputsRuleSet"
    priority = 3

    override_action {
      none {}
    }

    statement {
      managed_rule_group_statement {
        name        = "AWSManagedRulesKnownBadInputsRuleSet"
        vendor_name = "AWS"
      }
    }

    visibility_config {
      cloudwatch_metrics_enabled = true
      metric_name                = "AWSManagedRulesKnownBadInputsRuleSetMetric"
      sampled_requests_enabled   = true
    }
  }
  #----------------------- Rule:Admin Protection Rule Set -------------------------
  rule {
    name     = "AWSManagedRulesAdminProtectionRuleSet"
    priority = 4

    override_action {
      none {}
    }

    statement {
      managed_rule_group_statement {
        name        = "AWSManagedRulesAdminProtectionRuleSet"
        vendor_name = "AWS"
      }
    }

    visibility_config {
      cloudwatch_metrics_enabled = true
      metric_name                = "AWSManagedRulesAdminProtectionRuleSet"
      sampled_requests_enabled   = true
    }
  }
  #----------------------- Rule: Amazon Ip Reputation List -------------------------

  rule {
    name     = "AWSManagedRulesAmazonIpReputationList"
    priority = 5

    override_action {
      none {}
    }

    statement {
      managed_rule_group_statement {
        name        = "AWSManagedRulesAmazonIpReputationList"
        vendor_name = "AWS"
      }
    }

    visibility_config {
      cloudwatch_metrics_enabled = true
      metric_name                = "AWSManagedRulesAmazonIpReputationListMetric"
      sampled_requests_enabled   = true
    }
  }
  #----------------------- Rule: Anonymous Ip List -------------------------
  rule {
    name     = "AWSManagedRulesAnonymousIpList"
    priority = 6

    override_action {
      none {}
    }

    statement {
      managed_rule_group_statement {
        name        = "AWSManagedRulesAnonymousIpList"
        vendor_name = "AWS"
      }
    }

    visibility_config {
      cloudwatch_metrics_enabled = true
      metric_name                = "AWSManagedRulesAnonymousIpListMetric"
      sampled_requests_enabled   = true
    }
  }
  visibility_config {
    cloudwatch_metrics_enabled = true
    metric_name                = "WAFLoggingMetric"
    sampled_requests_enabled   = true
  }
}

resource "aws_wafv2_web_acl_logging_configuration" "waf_logging_configuration" {
  log_destination_configs = [aws_cloudwatch_log_group.wafv2-log-group.arn]
  resource_arn            = aws_wafv2_web_acl.webacl1.arn
  depends_on              = [aws_cloudwatch_log_group.wafv2-log-group]

  logging_filter {
    default_behavior = "KEEP"

    filter {
      behavior = "KEEP"

      condition {
        action_condition {
          action = "BLOCK"
        }
      }

      requirement = "MEETS_ALL"
    }

    filter {
      behavior = "DROP"

      condition {
        action_condition {
          action = "ALLOW"
        }
      }

      requirement = "MEETS_ALL"
    }
  }
}

resource "aws_cloudwatch_log_group" "wafv2-log-group" {
  name              = var.aws_cloudwatch_log_group_name
  retention_in_days = 14
}

resource "aws_wafv2_web_acl_association" "lb_waf_association" {
  resource_arn = data.aws_lb.aws-application-lb.arn
  web_acl_arn  = aws_wafv2_web_acl.webacl1.arn
}

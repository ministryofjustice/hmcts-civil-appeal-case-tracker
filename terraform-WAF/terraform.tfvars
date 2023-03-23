service_name = "casetracker"
region       = "eu-west-2"
#tacticalproducts

environments = {
  dev = {
    aws_lb_name       = "civil-LoadB-Y5O7JQURQ76D"
  },
  staging = {
    aws_lb_name       = "civil-LoadB-105UN4GBV5077"
  },
  prod = {
    aws_lb_name       = "civil-LoadB-QVBU457DP1B"
  },
}


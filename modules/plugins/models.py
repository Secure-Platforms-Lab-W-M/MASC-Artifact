from django.db import models

# Create your models here.
class PluginsList(models.Model):
    name = models.CharField(max_length=200)
    filename = models.CharField(max_length=100, db_index=True, default="any.properties")
    compilation = models.CharField(max_length=200, default='false')
    classfile = models.CharField(max_length=200, default='any.class')
    status = models.CharField(max_length=200, default='active')
    path = models.CharField(max_length=500)

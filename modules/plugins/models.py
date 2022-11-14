from django.db import models

# Create your models here.
class PluginsList(models.Model):
    name = models.CharField(max_length=200)
    filename = models.CharField(max_length=100, db_index=True, default="any.properties")
    path = models.CharField(max_length=500)

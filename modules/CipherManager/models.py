from django.db import models

class PropertiesList(models.Model):
    name = models.CharField(max_length=200)
    path = models.CharField(max_length=500)
    type = models.CharField(max_length=50)
    file = models.FileField()

# Create your models here.

from django.db import models

class PropertiesList(models.Model):
    name = models.CharField(max_length=200)
    filename = models.CharField(max_length=100, db_index=True, default="any.properties")
    path = models.CharField(max_length=500)
    type = models.CharField(max_length=50)

# Create your models here.

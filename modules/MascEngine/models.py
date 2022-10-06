from django.db import models

# Create your models here.
class MASCEngineProcesses(models.Model):
    id = models.CharField(max_length=200, primary_key=True)
    zip_file_name = models.CharField(max_length=100, db_index=True)
    properties = models.CharField(max_length=500)
    input_path = models.CharField(max_length=500)
    output_path = models.CharField(max_length=500)
    appName = models.CharField(max_length=50)
    scope = models.CharField(max_length=50)
    status = models.CharField(max_length=20)
from datetime import datetime

from django.db import models


# Create your models here.
class ProcessLog(models.Model):
    properties = models.CharField(max_length=500)
    scope = models.CharField(max_length=50)
    status = models.CharField(max_length=20)
    source_code = models.ForeignKey('SourceCode', on_delete=models.CASCADE)
    start_time = models.DateTimeField(default=datetime.now())


class SourceCode(models.Model):
    zip_file_name = models.CharField(max_length=100, db_index=True)
    zip_file_directory = models.CharField(max_length=100, db_index=True)
    input_path = models.CharField(max_length=500)
    output_path = models.CharField(max_length=500)
    appName = models.CharField(max_length=50, unique=True)

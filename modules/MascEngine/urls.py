from django.urls import path, re_path
from modules.MascEngine import views

app_name = "MascEngine"

urlpatterns = [
    path('', views.index, name='MascEngine'),
    path('history', views.runMASCEngine, name='MASCEngine'),
    path('deleteCode/<int:id>/<str:name>', views.delete_source_code, name='deleteCode')
]
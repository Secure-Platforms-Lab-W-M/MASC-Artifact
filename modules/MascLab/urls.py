from django.urls import path, re_path
from modules.MascLab import views

urlpatterns = [
    path('', views.index, name='MascLab'),
]
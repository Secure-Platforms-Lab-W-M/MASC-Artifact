from django.urls import path, re_path
from modules.plugins import views

urlpatterns = [
    path('', views.index, name='plugins'),
]
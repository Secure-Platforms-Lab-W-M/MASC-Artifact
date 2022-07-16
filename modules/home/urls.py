from django.urls import path, re_path
from modules.home import views

urlpatterns = [
    path('', views.index, name='home'),
]
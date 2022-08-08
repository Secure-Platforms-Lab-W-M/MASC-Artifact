from django.urls import path, re_path
from modules.UserProfile import views

urlpatterns = [
    path('', views.profile, name='profile'),
]
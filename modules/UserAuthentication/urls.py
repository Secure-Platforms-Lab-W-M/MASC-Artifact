from django.urls import path, re_path
from modules.UserAuthentication import views

urlpatterns = [
    path('login', views.login, name='login'),
    path('register', views.register, name='register')
]
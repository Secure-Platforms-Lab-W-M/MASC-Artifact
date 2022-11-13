from django.urls import path, re_path
from modules.toolprofile import views

urlpatterns = [
    path('', views.index, name='toolprofile'),
]
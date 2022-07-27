from django.urls import path, re_path
from modules.MascEngine import views

urlpatterns = [
    path('', views.index, name='MascEngine'),
    path('history', views.history, name='MascEngineHistory'),
]
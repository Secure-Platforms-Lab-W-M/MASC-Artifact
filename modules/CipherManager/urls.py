from django.urls import path, re_path
from modules.CipherManager import views

urlpatterns = [
    path('', views.index, name='PropertiesList'),
    path('upload/', views.uploadPropertyForm, name='uploadPropertyForm'),
    path('edit/<str:id>',views.editProperties, name='editProperties')
]
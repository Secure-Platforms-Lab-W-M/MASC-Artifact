from django.urls import path, re_path
from modules.plugins import views

urlpatterns = [
    path('', views.index, name='plugins'),
    path('upload/', views.uploadPlugins, name='uploadPropertyForm'),
    # path('delete/<str:id>', views.deletePlugins, name='deleteProperties')
]
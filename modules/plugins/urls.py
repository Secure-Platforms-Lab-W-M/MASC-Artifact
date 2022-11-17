from django.urls import path, re_path
from modules.plugins import views

urlpatterns = [
    path('', views.index, name='plugins'),
    path('upload/', views.uploadPlugins, name='uploadPropertyForm'),
    path('delete/<int:id>', views.delete_plugin, name='delete'),
    path('compile/<int:id>', views.compile_class, name='compile'),
    path('status/<int:id>', views.update_status, name='status'),
]
from django.urls import path, re_path
from modules.MascLab import views

urlpatterns = [
    path('', views.index, name='MascLab'),
    path('inputForm', views.input_Form, name='InputForm'),
    path('setup', views.set_up, name='setup')
]
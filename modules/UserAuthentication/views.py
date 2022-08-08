import re
from django.shortcuts import render

# Create your views here.
def login(request):
    return render(request, "Authentication/login.html")

def register(request):
    return render(request, "Authentication/register.html")
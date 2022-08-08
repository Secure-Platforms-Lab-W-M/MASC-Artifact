import profile
from urllib import request
from django.shortcuts import render

# Create your views here.
def profile(request):
    return render(request,"profile/profile.html")
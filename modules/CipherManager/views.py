import json
from django.shortcuts import render
def index(request):
    uploaded_files_header = ["Id", "File Name", "Path", "Type"]
    return render(request, "CipherManager/index.html", {
        "uploaded_files_header": uploaded_files_header
    })

def handle_uploaded_file(f):
    with open('./modules/static/properties/'+f.name, 'wb') as destination:
        for chunk in f.chunks():
            destination.write(chunk)

def uploadPropertyForm(request):
    if request.method == 'POST':
        print('ok')
        print(request.POST['name'])
        print(request.POST['type'])
        print(request.FILES)
        handle_uploaded_file(request.FILES['file'])
        return render(request,'CipherManager/thanks.html')
    return render(request, "CipherManager/uploadProperty.html")
# Create your views here.

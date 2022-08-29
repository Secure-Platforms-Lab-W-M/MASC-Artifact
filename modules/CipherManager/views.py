from django.shortcuts import render
def index(request):
    uploaded_files_header = ["Id", "File Name", "Path", "Type"]
    return render(request, "CipherManager/index.html", {
        "uploaded_files_header": uploaded_files_header
    })

def uploadPropertyForm(request):
    if request.method == 'POST':
        print('ok')
        print(request.body)
        return render(request,'CipherManager/thanks.html')
    return render(request, "CipherManager/uploadProperty.html")
# Create your views here.

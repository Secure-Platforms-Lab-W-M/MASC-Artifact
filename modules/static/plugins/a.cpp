#include<iostream>
#include<algorithm>
using namespace std;
int main(){
  int t;
  cin>>t;
  while(t--){
    int arr[3],n;
    for(int i=0;i<3;i++) {
            cin>>arr[i];
    }
    cin>>n;
    sort(arr,arr+3); n=n-(arr[2]-arr[1]); n=n-(arr[2]-arr[0]);
   // cout<<n<<endl;
    if(n>=0 && n%3==0) cout<<"YES"<<endl;
    else cout<<"NO"<<endl;
  }
}

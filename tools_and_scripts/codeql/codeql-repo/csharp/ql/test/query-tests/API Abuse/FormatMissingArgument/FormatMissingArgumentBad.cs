using System;

class Bad
{
    void Hello(string first, string last)
    {
        Console.WriteLine("Hello {0} {1}", first);
        Console.WriteLine("Hello {1} {2}", first, last);
    }
}

int main()
{
  int abc;
  int b;

  abc = 0;
  b = 10;

  {
    int b;
    b = 20;
    a = a + b;
  }

  return a;
}

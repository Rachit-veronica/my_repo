def pattern(n):
    k= 2 * n - 2
    for i in range(0,n):
        for j in range(0,k):
            print(end=" ")
        k=k-1
        for j in range(0, i+1):
            print("* ",end="")
        print("\n")

a=int(input("enter the line to be printed: "))
pattern(a)

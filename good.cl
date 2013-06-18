class Main { main() : Int {0 }; };

class A {
	a:Int <- a;
	inky():Int { 1 };
};

class B inherits A {
	binky():String {"hello"};
};

class C {
	b:B;
	winky():Int { 
	 let x:Int <- 1 in  

b@B.inky() };
};

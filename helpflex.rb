mop = <<MOP
OR   ||
AND  &&
EQ   ==
NE   !=
LE   <=
GE   >=
SHL  <<
SHR  >>
INC  ++
DEC  --
PTR  ->
ELLIPSIS ...
MUL_ASSIGN  *=
DIV_ASSIGN  /=
MOD_ASSIGN  %=
ADD_ASSIGN  +=
SUB_ASSIGN  -=
SHL_ASSIGN  <<=
SHR_ASSIGN  >>=
AND_ASSIGN  &=
XOR_ASSIGN  ^=
OR_ASSIGN   |=
MOP

mop.each_line { |line|
	value, key = line.split(" ")
	puts "\"#{key}\"  { return tok(#{value}); } "
}

-------------StaticDataFrag---------------
compiler.translate.DataFrag@3e3b6297
---------CU0----------
 = call L2 []
---------CU1----------
L2:
Enter L2
T3 = 10000
sp[0] = T3
T4 = 0
sp[4] = T4
T5 = 2800
sp[8] = T5
T6 = 0
sp[12] = T6
T7 = 0
sp[16] = T7
T10 = 11204
T9 = call L1 [T10]
T11 = T9
sp[20] = T11
T12 = 0
sp[24] = T12
L3:
T14 = sp[4]
T15 = sp[8]
T13 = T14 - T15
T16 = T13
IfFalse T16 Goto L5
T18 = sp[4]
T19 = sp[4]
T17 = T19 + 1
T20 = T17
sp[4] = T20
T22 = 4
T21 = T22 * T18
T23 = sp[20]
T24 = T23 + T21
T26 = sp[0]
T25 = T26 / 5
T27 = T25
T24[0] = T27
L4:
Goto L3
L5:
L6:
T28 = 0
sp[12] = T28
T30 = sp[8]
T29 = T30 * 2
T31 = T29
sp[24] = T31
T32 = sp[24]
IfFalse T32 Goto L8
T33 = sp[8]
sp[4] = T33
L9:
T35 = 4
T36 = sp[4]
T34 = T35 * T36
T37 = sp[20]
T38 = T37 + T34
T40 = T38[0]
T41 = sp[0]
T39 = T40 * T41
T43 = sp[12]
T42 = T43 + T39
T44 = T42
sp[12] = T44
T46 = 4
T47 = sp[4]
T45 = T46 * T47
T48 = sp[20]
T49 = T48 + T45
T51 = sp[24]
T50 = T51 - 1
T52 = T50
sp[24] = T52
T54 = sp[12]
T55 = sp[24]
T53 = T54 % T55
T56 = T53
T49[0] = T56
T58 = sp[24]
T59 = sp[24]
T57 = T59 - 1
T60 = T57
sp[24] = T60
T62 = sp[12]
T61 = T62 / T58
T63 = T61
sp[12] = T63
T65 = sp[4]
T64 = T65 - 1
T66 = T64
sp[4] = T66
T67 = sp[4]
IfFalse T67 Goto L11
L10:
T69 = sp[12]
T70 = sp[4]
T68 = T69 * T70
T71 = T68
sp[12] = T71
Goto L9
L11:
L7:
T73 = sp[8]
T72 = T73 - 14
T74 = T72
sp[8] = T74
T75 = L12
T77 = sp[12]
T78 = sp[0]
T76 = T77 / T78
T80 = sp[16]
T79 = T80 + T76
T81 = call L0 [T75, T79]
T83 = sp[12]
T84 = sp[0]
T82 = T83 % T84
T85 = T82
sp[16] = T85
Goto L6
L8:
T86 = 0
return T86
Leave L2
Leave L2

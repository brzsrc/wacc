r4 in integer
r5 in integer
freed
freed
r4 in integer
r5 in integer
freed
freed
r4 in integer
r5 in integer
freed
freed
r4 in integer
r5 in integer
freed
freed
r4 in integer
r5 in integer
freed
freed
r4 in integer
r5 in integer
freed
freed
r4 in integer
r5 in integer
freed
freed
r4 in integer
r5 in integer
freed
freed
r4 in integer
freed
r4 in Ident
r5 in Ident
freed
r5 in Ident
freed
r5 in Ident
freed
r5 in Ident
freed
r5 in Ident
freed
r5 in Ident
freed
r5 in Ident
freed
r5 in Ident
freed
	.data


	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #36
		LDR r4, =2
		LDR r5, =1
		ADD r5, r5, r4
		STR r4, [sp, #0]
		LDR r4, =4
		LDR r5, =3
		ADD r5, r5, r4
		STR r4, [sp, #4]
		LDR r4, =6
		LDR r5, =5
		ADD r5, r5, r4
		STR r4, [sp, #8]
		LDR r4, =8
		LDR r5, =7
		ADD r5, r5, r4
		STR r4, [sp, #12]
		LDR r4, =10
		LDR r5, =9
		ADD r5, r5, r4
		STR r4, [sp, #16]
		LDR r4, =12
		LDR r5, =11
		ADD r5, r5, r4
		STR r4, [sp, #20]
		LDR r4, =14
		LDR r5, =13
		ADD r5, r5, r4
		STR r4, [sp, #24]
		LDR r4, =16
		LDR r5, =15
		ADD r5, r5, r4
		STR r4, [sp, #28]
		LDR r4, =17
		STR r4, [sp, #32]
		LDR r4, [sp, #4]
		LDR r5, [sp, #0]
		ADD r5, r5, r4
		LDR r5, [sp, #8]
		ADD r4, r4, r5
		LDR r5, [sp, #12]
		ADD r4, r4, r5
		LDR r5, [sp, #16]
		ADD r4, r4, r5
		LDR r5, [sp, #20]
		ADD r4, r4, r5
		LDR r5, [sp, #24]
		ADD r4, r4, r5
		LDR r5, [sp, #28]
		ADD r4, r4, r5
		LDR r5, [sp, #32]
		ADD r4, r4, r5
		MOV r0, r4
		BL exit
		ADD sp, sp, #36
		LDR r0, =0
		POP {pc}


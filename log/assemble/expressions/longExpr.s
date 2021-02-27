r4 in integer
r5 in integer
freed
r5 in integer
freed
r5 in integer
freed
r5 in integer
freed
r5 in integer
freed
r5 in integer
freed
r5 in integer
freed
r5 in integer
freed
r5 in integer
freed
r5 in integer
freed
r5 in integer
freed
r5 in integer
freed
r5 in integer
freed
r5 in integer
freed
r5 in integer
freed
r5 in integer
freed
freed
r4 in Ident
	.data


	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #4
		LDR r4, =17
		LDR r5, =16
		ADD r5, r5, r4
		LDR r5, =15
		ADD r5, r5, r4
		LDR r5, =14
		ADD r5, r5, r4
		LDR r5, =13
		ADD r5, r5, r4
		LDR r5, =12
		ADD r5, r5, r4
		LDR r5, =11
		ADD r5, r5, r4
		LDR r5, =10
		ADD r5, r5, r4
		LDR r5, =9
		ADD r5, r5, r4
		LDR r5, =8
		ADD r5, r5, r4
		LDR r5, =7
		ADD r5, r5, r4
		LDR r5, =6
		ADD r5, r5, r4
		LDR r5, =5
		ADD r5, r5, r4
		LDR r5, =4
		ADD r5, r5, r4
		LDR r5, =3
		ADD r5, r5, r4
		LDR r5, =2
		ADD r5, r5, r4
		LDR r5, =1
		ADD r5, r5, r4
		STR r4, [sp, #0]
		LDR r4, [sp, #0]
		MOV r0, r4
		BL exit
		ADD sp, sp, #4
		LDR r0, =0
		POP {pc}


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
		LDR r4, =2
		LDR r5, =1
		SUB r5, r5, r4
		LDR r5, =3
		ADD r4, r4, r5
		LDR r5, =4
		SUB r4, r4, r5
		LDR r5, =5
		ADD r4, r4, r5
		LDR r5, =6
		SUB r4, r4, r5
		LDR r5, =7
		ADD r4, r4, r5
		LDR r5, =8
		SUB r4, r4, r5
		LDR r5, =9
		ADD r4, r4, r5
		LDR r5, =10
		SUB r4, r4, r5
		LDR r5, =11
		ADD r4, r4, r5
		LDR r5, =12
		SUB r4, r4, r5
		LDR r5, =13
		ADD r4, r4, r5
		LDR r5, =14
		SUB r4, r4, r5
		LDR r5, =15
		ADD r4, r4, r5
		LDR r5, =16
		SUB r4, r4, r5
		LDR r5, =17
		ADD r4, r4, r5
		STR r4, [sp, #0]
		LDR r4, [sp, #0]
		MOV r0, r4
		BL exit
		ADD sp, sp, #4
		LDR r0, =0
		POP {pc}


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
freed
r4 in integer
freed
r4 in Ident
r5 in Ident
freed
r5 in Ident
r6 in Ident
freed
freed
freed
r4 in Ident
r5 in Ident
freed
r5 in Ident
r6 in Ident
freed
freed
r5 in integer
freed
freed
r4 in Ident
r5 in Ident
freed
r5 in Ident
r6 in Ident
freed
freed
	.data


	msg_1:
		.word 5
		.ascii "\0"
	msg_0:
		.word 7
		.ascii "%d\0"
	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #16
		LDR r4, =2
		LDR r5, =1
		ADD r5, r5, r4
		LDR r5, =3
		ADD r4, r4, r5
		LDR r5, =4
		ADD r4, r4, r5
		LDR r5, =5
		ADD r4, r4, r5
		LDR r5, =6
		ADD r4, r4, r5
		LDR r5, =7
		ADD r4, r4, r5
		LDR r5, =8
		ADD r4, r4, r5
		LDR r5, =9
		ADD r4, r4, r5
		LDR r5, =10
		ADD r4, r4, r5
		LDR r5, =11
		ADD r4, r4, r5
		LDR r5, =12
		ADD r4, r4, r5
		LDR r5, =13
		ADD r4, r4, r5
		LDR r5, =14
		ADD r4, r4, r5
		LDR r5, =15
		ADD r4, r4, r5
		LDR r5, =16
		ADD r4, r4, r5
		LDR r5, =17
		ADD r4, r4, r5
		STR r4, [sp, #0]
		LDR r4, =2
		LDR r5, =-1
		SUB r5, r5, r4
		LDR r5, =3
		SUB r4, r4, r5
		LDR r5, =4
		SUB r4, r4, r5
		LDR r5, =5
		SUB r4, r4, r5
		LDR r5, =6
		SUB r4, r4, r5
		LDR r5, =7
		SUB r4, r4, r5
		LDR r5, =8
		SUB r4, r4, r5
		LDR r5, =9
		SUB r4, r4, r5
		LDR r5, =10
		SUB r4, r4, r5
		LDR r5, =11
		SUB r4, r4, r5
		LDR r5, =12
		SUB r4, r4, r5
		LDR r5, =13
		SUB r4, r4, r5
		LDR r5, =14
		SUB r4, r4, r5
		LDR r5, =15
		SUB r4, r4, r5
		LDR r5, =16
		SUB r4, r4, r5
		LDR r5, =17
		SUB r4, r4, r5
		STR r4, [sp, #4]
		LDR r4, =2
		LDR r5, =1
		MUL r5, r5, r4
		LDR r5, =3
		MUL r4, r4, r5
		LDR r5, =4
		MUL r4, r4, r5
		LDR r5, =5
		MUL r4, r4, r5
		LDR r5, =6
		MUL r4, r4, r5
		LDR r5, =7
		MUL r4, r4, r5
		LDR r5, =8
		MUL r4, r4, r5
		LDR r5, =9
		MUL r4, r4, r5
		LDR r5, =10
		MUL r4, r4, r5
		STR r4, [sp, #8]
		LDR r4, =10
		STR r4, [sp, #12]
		LDR r4, [sp, #12]
		LDR r5, [sp, #8]
		MOV r0, r5
		MOV r1, r5
		BL p_check_divide_by_zero
		BL __aeabi_idiv
		MOV r0, r5
		LDR r5, [sp, #4]
		LDR r6, [sp, #0]
		ADD r6, r6, r5
		ADD r5, r5, r4
		MOV r0, r4
		BL p_print_int
		BL p_print_ln
		LDR r4, [sp, #12]
		LDR r5, [sp, #8]
		MOV r0, r5
		MOV r1, r5
		BL p_check_divide_by_zero
		BL __aeabi_idiv
		MOV r0, r5
		LDR r5, [sp, #4]
		LDR r6, [sp, #0]
		ADD r6, r6, r5
		ADD r5, r5, r4
		LDR r5, =256
		MOV r0, r4
		MOV r1, r4
		BL p_check_divide_by_zero
		BL __aeabi_idivmod
		MOV r1, r4
		MOV r0, r4
		BL p_print_int
		BL p_print_ln
		LDR r4, [sp, #12]
		LDR r5, [sp, #8]
		MOV r0, r5
		MOV r1, r5
		BL p_check_divide_by_zero
		BL __aeabi_idiv
		MOV r0, r5
		LDR r5, [sp, #4]
		LDR r6, [sp, #0]
		ADD r6, r6, r5
		ADD r5, r5, r4
		MOV r0, r4
		BL exit
		ADD sp, sp, #16
		LDR r0, =0
		POP {pc}
	p_print_int:
		PUSH {lr}
		MOV r1, r0
		LDR r0, =msg_0
		ADD r0, r0, #4
		BL printf
		MOV r0, #0
		BL fflush
		POP {pc}
	p_print_ln:
		PUSH {lr}
		LDR r0, =msg_1
		ADD r0, r0, #4
		BL puts
		MOV r0, #0
		BL fflush
		POP {pc}


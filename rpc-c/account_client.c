
#include <stdbool.h>
#include "account.h"

#define CREATE_OP 0
#define WITHDRAW_OP 1
#define DEPOSIT_OP 2
#define BALANCE_OP 3

float ERROR_VALUE_FLOAT = -1;
int ERROR_VALUE_INT = -1;

void
dum_prog_1(char *host)
{
	CLIENT *clnt;

#ifndef	DEBUG
	clnt = clnt_create (host, ACC_PROG, ACC_VERS, "udp");
	if (clnt == NULL) {
		clnt_pcreateerror (host);
		exit (1);
	}
#endif	/* DEBUG */

	int choice;

	do
	{
		printf("==========================\n");
		printf("====Account Operations====\n");
		printf("==========================\n");
		printf("0. create account \n");
		printf("1. withdraw from account\n");
		printf("2. deposit into account\n");
		printf("3. get balance from account\n");
		printf("==========================\n");
		printf("Choice: ");
		scanf("%d", &choice);
		printf("==========================\n");
		
		if(choice == CREATE_OP)
		{
			
			float initial_balance;
			printf("Initial balance of account: ");
			scanf("%f", &initial_balance);

 			int *result_create = create_account_1(&initial_balance, clnt);
			
			if (result_create == (int *) NULL) 
			{
				clnt_perror (clnt, "call failed");
			}
			else
			{
				printf("\n");
				printf("Created account with id: %d", *result_create);
				printf("\n");
			}
		}
		else if(choice == WITHDRAW_OP)
		{
			float value;
			int account_id;
			
			printf("Value: ");
			scanf("%f", &value);

			printf("Account id: ");
			scanf("%d", &account_id);

			bank_operation operation;
			operation.account_id = account_id;
			operation.value = value;


 			float *result = withdraw_1(&operation, clnt);
			
			if (result == (float *) NULL) 
			{
				clnt_perror (clnt, "call failed");
			}
			else
			{
				printf("\n");
				printf("Balance withdrew: %f", *result);
				printf("\n");
			}
		}		
		else if(choice == DEPOSIT_OP)
		{
			float value;
			int account_id;
			
			printf("Value: ");
			scanf("%f", &value);

			printf("Account id: ");
			scanf("%d", &account_id);

			bank_operation operation;
			operation.account_id = account_id;
			operation.value = value;


 			float *result = deposit_1(&operation, clnt);
			
			if (result == (float *) NULL) 
			{
				clnt_perror (clnt, "call failed");
			}
			else
			{
				printf("\n");
				printf("Balance deposited: %f", *result);
				printf("\n");
			}
			
		}
		else if(choice == BALANCE_OP)
		{
			int account_id;
			printf("Account id: ");
			scanf("%d", &account_id);

 			float *result = balance_1(&account_id, clnt);
			
			if (result == (float *) NULL) 
			{
				clnt_perror (clnt, "call failed");
			}
			else
			{
				printf("\n");
				printf("Balance: %f", *result);
				printf("\n");
			}
		}
		else
		{
			printf("Invalid Choice. Try Again.\n\n");
		}
	}while(true);	

	
#ifndef	DEBUG
	clnt_destroy (clnt);
#endif	 /* DEBUG */
}


int
main (int argc, char *argv[])
{
	char *host;

	if (argc < 2) {
		printf ("usage: %s server_host\n", argv[0]);
		exit (1);
	}
	host = argv[1];
	dum_prog_1 (host);
exit (0);
}

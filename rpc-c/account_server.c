#include <stdbool.h>
#include <stdlib.h>
#include "account.h"

#define ACCOUNTS_LIMIT 100

float *accounts;
int pointer;

float ERROR_VALUE_FLOAT = -1;
int ERROR_VALUE_INT = -1;

bool
has_initialized_accounts () 
{
    return accounts != NULL;
}

void 
initialize_accounts () 
{
    accounts = (float *) calloc (ACCOUNTS_LIMIT, sizeof (float));
    pointer = 0;
}

bool exceeded_accounts_limit () 
{
    return pointer >= ACCOUNTS_LIMIT;
}

bool account_exists_from_int (int account_id) {
    return account_id < pointer && account_id >= 0;
}

bool account_exists (bank_operation *operation) {
    return account_exists_from_int(operation->account_id);
}


int *
create_account_1_svc(float* initial_balance, struct svc_req *rqstp)
{
    if (!has_initialized_accounts()) 
    {
        printf("INFO: initializing accounts\n");
        initialize_accounts();
    }

    if (exceeded_accounts_limit())
    {
        /* already exceeded the number of accounts that can be created */
        printf("WARN: already exceeded the number of accounts that can be created\n");
        return &ERROR_VALUE_INT;
    }

    printf("INFO: creating account\n");

    static int resultint_id;

    int id = pointer++;
    accounts[id] = *initial_balance;

    resultint_id = id;
    return &resultint_id;
}

float *
withdraw_1_svc(bank_operation* operation, struct svc_req *rqstp)
{
    if (!account_exists(operation)) 
    {
        printf("ERROR: attempt of withdrawing from a non existent account\n");
        return &ERROR_VALUE_FLOAT;
    }

    float existing_value = accounts[operation->account_id];
    float withdrawn_value = operation->value;
    
    if (existing_value < withdrawn_value)
    {
        printf("ERROR: attempt of withdrawing more value from an existing account than allowed\n");
        return &ERROR_VALUE_FLOAT;
    }

    printf("Withdrawing from account\n");
    static float resulting_value;

    float resulting_balance = existing_value - withdrawn_value;
    accounts[operation->account_id] = resulting_balance;

    resulting_value = resulting_balance;
    return &resulting_value;
}

float *
deposit_1_svc(bank_operation* operation, struct svc_req *rqstp)
{
    if (!account_exists(operation)) 
    {
        printf("ERROR: attempt of depositing in a non existent account\n");
        return &ERROR_VALUE_FLOAT;
    }

    if (operation->value <= 0)
    {
        printf("ERROR: Attempt of depositing a negative value into an account\n");
        return &ERROR_VALUE_FLOAT; 
    }

    printf("Depositing into an account\n");
    static float resulting_value;

    float existing_value = accounts[operation->account_id];
    float resulting_balance = existing_value + operation->value;
    accounts[operation->account_id] = resulting_balance;

    resulting_value = resulting_balance;
    return &resulting_value;

}

float *
balance_1_svc(int* account_id, struct svc_req *rqstp)
{
    if (!account_exists_from_int(*account_id)) 
    {
        printf("ERROR: attempt of getting the balance from a non existent account\n");
        return &ERROR_VALUE_FLOAT;
    }

    static float resulting_value;
    resulting_value = accounts[*account_id];

    return &resulting_value;
}



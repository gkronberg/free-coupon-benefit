BEGIN;            
    TRUNCATE TABLE public.void RESTART IDENTITY CASCADE;
    TRUNCATE TABLE public.refund RESTART IDENTITY CASCADE;
    TRUNCATE TABLE public.authorizations RESTART IDENTITY CASCADE;
    TRUNCATE TABLE public.purchases RESTART IDENTITY CASCADE;
    TRUNCATE TABLE public.transactions RESTART IDENTITY CASCADE; -- this includes tables: purchases, authorizations, refund, void
    TRUNCATE TABLE public.gateways RESTART IDENTITY CASCADE;
    TRUNCATE TABLE public.gateway_conditions RESTART IDENTITY CASCADE;
    TRUNCATE TABLE public.gateway_log RESTART IDENTITY CASCADE;
    TRUNCATE TABLE public.gateway_condition_log RESTART IDENTITY CASCADE;
    DELETE FROM public.gateway_scenarios WHERE id != 0;
    UPDATE public.gateway_scenarios SET active = true WHERE id = 0;
    TRUNCATE TABLE public.messages RESTART IDENTITY CASCADE;
    TRUNCATE TABLE public.message_categories RESTART IDENTITY CASCADE;
    TRUNCATE TABLE public.minimum_amounts_without_charge RESTART IDENTITY CASCADE;
    TRUNCATE TABLE public.delayed_transaction RESTART IDENTITY CASCADE;
    TRUNCATE TABLE public.new_gateway_messages RESTART IDENTITY CASCADE;
    TRUNCATE TABLE public.ally_token RESTART IDENTITY CASCADE;
    TRUNCATE TABLE public.alt_payment_method_transactions RESTART IDENTITY CASCADE;
    TRUNCATE TABLE public.orders_on_credit RESTART IDENTITY CASCADE;
    TRUNCATE TABLE public.orders_on_credit_experiment RESTART IDENTITY CASCADE;
    TRUNCATE TABLE public.payment_charge_data RESTART IDENTITY CASCADE;
    TRUNCATE TABLE public.payment_trust_rules RESTART IDENTITY CASCADE;
    TRUNCATE TABLE public.rappipay_refund_rules RESTART IDENTITY CASCADE;
    TRUNCATE TABLE public.partial_refund_rules RESTART IDENTITY CASCADE;
    TRUNCATE TABLE public.payment_trust_store_type_config RESTART IDENTITY CASCADE;
    TRUNCATE TABLE public.execution_flow RESTART IDENTITY CASCADE;
    TRUNCATE TABLE public.transaction_retry RESTART IDENTITY CASCADE;
    TRUNCATE TABLE public.historic_charge_data RESTART IDENTITY CASCADE;
    TRUNCATE TABLE public.ally RESTART IDENTITY CASCADE;
    TRUNCATE TABLE public.traffic_log_data RESTART IDENTITY CASCADE;
    TRUNCATE TABLE public.gateway_error_category;
    TRUNCATE TABLE public.provider_trace;
    TRUNCATE TABLE public.cibc_currency_rates_history;
COMMIT;
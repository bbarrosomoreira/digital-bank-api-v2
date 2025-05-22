INSERT INTO politica_de_taxas (
    categoria, tarifa_manutencao_mensal_conta_corrente, rendimento_percentual_mensal_conta_poupanca,
	tarifa_manutencao_conta_internacional,
    limite_cartao_credito, limite_diario_debito, tarifa_seguro_viagem,
	tarifa_seguro_fraude,
	valor_apolice_fraude, valor_apolice_viagem
) VALUES
('COMUM', 12.00, 0.005, 0.00, 1000.00, 500.00, 50.00, 0.00, 5000.00, 10000.00),
('SUPER', 8.00, 0.007, 0.00, 5000.00, 2500.00, 50.00, 0.00, 5000.00, 10000.00),
('PREMIUM', 0.00, 0.009, 0.00, 10000.00, 5000.00, 0.00, 0.00, 5000.00, 10000.00);
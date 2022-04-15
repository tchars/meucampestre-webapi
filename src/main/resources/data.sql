-- INSERT INTO `roles` (id, nome)
-- VALUES (1, 'ROLE_ADMIN'), (2, 'ROLE_SINDICO'), (3, 'ROLE_CONDOMINO');

-- INSERT INTO `usuarios`(`id`, `documento`, `email`, `nome`, `senha`)
-- VALUES (1,'12345678901','administrador@meucampestre.com.br','Administrador','$2a$10$YfqQrjT0.0D86y.j1I9ZT.tkeu0R4waghbgctG.4i1dAdRpyQk8Sa'),
--        (2,'12345678902','sindico@meucampestre.com.br','Síndico','$2a$10$YfqQrjT0.0D86y.j1I9ZT.tkeu0R4waghbgctG.4i1dAdRpyQk8Sa'),
--        (3,'12345678903','condomino@meucampestre.com.br','Condômino','$2a$10$YfqQrjT0.0D86y.j1I9ZT.tkeu0R4waghbgctG.4i1dAdRpyQk8Sa'); --senha:senha
--
-- INSERT INTO `usuarios_roles`(`usuario_id`, `role_id`) VALUES ('1','1'), ('2','2'), ('3','3');

INSERT IGNORE INTO `roles` (id, nome) VALUES (1, 'ADMIN'), (2, 'SINDICO'), (3, 'CONSELHEIRO'), (4, 'PORTEIRO'), (5, 'CONDOMINO');
UPDATE usuario
SET
    email = 'novoemail@exemplo.com',
    username = 'novo_username',
    senha_hash = 'novo_hash_'
WHERE username = 'username_atual';

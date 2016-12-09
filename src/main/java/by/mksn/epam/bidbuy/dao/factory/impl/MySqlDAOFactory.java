package by.mksn.epam.bidbuy.dao.factory.impl;

import by.mksn.epam.bidbuy.dao.UserDAO;
import by.mksn.epam.bidbuy.dao.factory.DAOFactory;
import by.mksn.epam.bidbuy.dao.impl.MySqlUserDAO;

/**
 * Concrete DAO factory for MySQL database
 */
public class MySqlDAOFactory extends DAOFactory {

    private static final MySqlDAOFactory instance = new MySqlDAOFactory();
    private UserDAO userDAO;

    private MySqlDAOFactory() {
        userDAO = new MySqlUserDAO();
    }

    public static MySqlDAOFactory getInstance() {
        return instance;
    }

    @Override
    public UserDAO getUserDAO() {
        return userDAO;
    }
}

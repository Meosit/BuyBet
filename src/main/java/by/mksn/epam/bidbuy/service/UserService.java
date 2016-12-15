package by.mksn.epam.bidbuy.service;

import by.mksn.epam.bidbuy.entity.User;
import by.mksn.epam.bidbuy.service.exception.ServiceException;
import by.mksn.epam.bidbuy.service.exception.ServiceUserException;

/**
 * Service which provides authorization and registration api for users.
 */
public interface UserService {

    /**
     * Creates new user with the specified credentials
     *
     * @param username username of user, must be unique
     * @param email    email of user, must be unique
     * @param password password of new user
     * @return {@link User} object which represents registered user
     * @throws ServiceUserException if user with specified credentials is already exists
     * @throws ServiceException     if error happens during execution
     */
    User register(String username, String email, String password) throws ServiceException;

    /**
     * Authorizes user with the given credentials.
     *
     * @param username username of a user
     * @param password password of a user
     * @return - {@code null} if user with the given credentials does not exists <br> - {@link User} entity of authorized user.
     * @throws ServiceUserException if password does not match
     * @throws ServiceException     if error happens during execution
     */
    User login(String username, String password) throws ServiceException;

    /**
     * Updates user data
     *
     * @param updatedUser updated user entity which will be stored into database
     * @throws ServiceException if error happens during execution
     */
    void updateUser(User updatedUser) throws ServiceException;

}
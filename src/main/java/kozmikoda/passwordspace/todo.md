# To do list

## Exceptions

### Create 'exceptions' directory for these classes
### Create all the custom exceptions that handle most of the runtime errors

## Storing service passwords

### Encrypt each service password before storing to the database
#### 1-) Get the user plain password text
#### 2-) Encrypt the plain service password text using user password as a key (not its hash!)
#### 3-) Store the return value of the encryption function

## Utility Functions

### Create 'util' directory for these classes
### Put all utility functions inside their class as static members
### If a utility function can be used from different applications make it available to them

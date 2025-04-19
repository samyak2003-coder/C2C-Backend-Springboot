### Changes

1. Filtering
2. Deleted Entities files, renamed classes and move to their respective folders
3. Implemented Facade Pattern for Orders and Products
    - Both orders and products needed methods of each other
    - Leads to tight coupling
4. Implemented interfaces for services
5. UI Changes
6. AuthToken is stored in localStorage instead of Cookies.
7. JWT Middleware instead of checking token in every file
8. apiCaller.js instead of writing code for calling API in every page
9. Enums instead of writing Strings for Product status
10. When offer is accepted, the other offers are automatically rejected
11. Separated Controllers into multiple files and folders.
12. DTO's 
13. Common Exceptions were moved to folders
14. Search functionallity
15. Moved stuff to configs -> Dotenv, WebConfig
16. Design approaches

# zhcq-co
A fashion ecommerce platform

### IS3106 Group Project (Group 11)

## Instructions:
- Please run the Ionic application in mobile mode on the browser for a better experience
- Please use alternateDocRoot found in this folder as the alternatedocroot_1 when you deploy the project. Please add the following lines to:
  - glassfish.xml:
    ```
    <property name="alternatedocroot_1" value="from=/images/* dir=YOUR_PATH_HERE\zhcq-co\alternateDocRoot"/>
    ```
  - web.xml:
    ```
    <context-param>
        <param-name>alternatedocroot_1</param-name>
        <param-value>YOUR_PATH_HERE\zhcq-co\alternateDocRoot\images</param-value>
    </context-param>
    ```     
    


# INTO-CPS Diagrams  

Two specialized diagrams have been defined within the INTO-CPS project to support system modeling:  

---

## 1. Architecture Diagram  
- **Purpose**: Specializes the SysML block diagram to specify a system's architecture in terms of its components.  
- **Creation**:  
  1. Right-click on a **Package** in the Modelio project explorer.  
  2. Select **Create Diagram** > **Architecture Diagram** from the context menu.  
  3. Name the diagram and start defining the system architecture.  

![1]

*Screenshot: Creating an Architecture Diagram.*  

---

## 2. Connection Diagram  
- **Purpose**: Specializes the SysML internal block diagram to describe the internal configuration and connections between system components.  
- **Creation**:  
  1. Right-click on a **SysML Block** in the Modelio project explorer.  
  2. Select **Create Diagram** > **Connection Diagram** from the context menu.  
  3. Name the diagram and define the internal connections.  

![2]

*Screenshot: Creating a Connection Diagram.*  

---



# FMI Features  

The INTO-CPS module provides two commands to integrate SysML modeling with the **Functional Mock-up Interface (FMI)** standard, specifically the `modelDescription.xml` file defined in the [FMI 2.0 Specification][5].  

---

## 1. FMI Import  
- **Purpose**: Imports a `modelDescription.xml` file to create a corresponding SysML Block in Modelio.  
- **Steps**:  
  1. Right-click on a **Package** in the Modelio project explorer.  
  2. Select **INTO-CP module** > **Reverse FMI** from the context menu.  
  3. Browse and select the `modelDescription.xml` file to import.  

![3]

*Screenshot: Importing an FMI file.*  

---

## 2. FMI Export  
- **Purpose**: Exports a SysML Block as a `modelDescription.xml` file. Note that this only exports the interface description, not the behavior.  
- **Steps**:  
  1. Right-click on a **SysML Block** in the Modelio project explorer.  
  2. Select **INTO-CPS module** > **Generate FMI** from the context menu.  
  3. Choose the destination folder and save the file.  

![4]

*Screenshot: Exporting an FMI file.*  

---

# Connection Export  

## Generating a Configuration from a BlockInstance  
- **Purpose**: Exports the configuration of a `BlockInstance` as a Maestro co-simulation configuration file in JSON format.  
- **Steps**:  
  1. Select the desired `BlockInstance` in the Modelio project explorer.  
  2. Right-click and choose **INTO-CPS** > **Generate Configuration**.  
  3. Provide a relevant name for the configuration file.  
  4. Click **Generate** to create the JSON file.  

![6]

*Screenshot: Generating a configuration.*  

![7]

*Screenshot: Naming the configuration file.*  

---

## References  
- [FMI 2.0 Specification][5]: Official documentation for the Functional Mock-up Interface standard.  

[1]: img/architecture.png  
[2]: img/connection.png  
[3]: img/reversefmi.png  
[4]: img/generatefmi.png  
[5]: https://www.fmi-standard.org/downloads  
[6]: img/sysml-generateconfig.png  
[7]: img/sysml-configuration-name.png  

---
 
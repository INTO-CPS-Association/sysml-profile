# INTO-CPS module for Modelio 


## Description  

**Modelio** is a powerful, open-source modeling tool that supports **UML**, **BPMN**, and **SysML** for system modeling. It is highly extensible, allowing users to add custom modules or use existing ones to tailor the tool to their specific needs.  

### Key Features:  
- **SysML support**: Integrated with UML and BPMN for comprehensive system modeling.  
- **XMI import/export**: Enables interoperability with other modeling tools.  
- **Scripting support**: Built-in Jython scripting for automation and customization.  
- **Extensibility**: Add modules to support additional languages, methodologies, or modeling techniques.  

For community support, visit the [Modelio Forum](http://www.modelio.org/forum/index.html).  

## License  
Modelio is **open-source software**. The core application is licensed under the **GNU GPL**, while the module runtime (used for developing extensions) is licensed under the **Apache License**. This provides flexibility for reuse and embedding.  

For full licensing details, visit the [Modelio License Page](http://www.modelio.org/about-modelio/license.html).  

---

## Download and Installation  

### Current Version  
The latest stable version of Modelio is **5.4.1**.  

### Download  
Binary distributions for **Windows** and **Linux** are available on the [Modelio Download Page](http://www.modelio.org/downloads/download-modelio.html).  

### Installation Instructions  
1. Download the appropriate binary archive for your operating system.  
2. Extract the archive to your desired installation directory.  
3. Follow the instructions in the [Modelio Quick Start Guide](https://github.com/ModelioOpenSource/Modelio/wiki) for setup and configuration.  

For version-specific details, refer to the [Modelio Version History](https://github.com/ModelioOpenSource/Modelio/releases).  

---

## Installing the INTO-CPS Module  

The **INTO-CPS module** extends Modelio's capabilities for system modeling, particularly for cyber-physical systems.  

### Resources  
- **Documentation**: [INTO-CPS Documentation](todo) *(link to be updated)*.  
- **Latest Releases**: [INTO-CPS Releases](https://github.com/INTO-CPS-Association/sysml-profile/releases).  
- **Issue Reporting**: [Report an Issue](https://github.com/INTO-CPS-Association/sysml-profile/issues/new).  

### Installation Steps  
1. Download the latest INTO-CPS module (`.jmdac` file) from the [Releases Page](https://github.com/INTO-CPS-Association/sysml-profile/releases).  
2. Add the module to the Modelio module catalog.  

---

## Adding the INTO-CPS Module to Modelio  

### Step 1: Open the Modules Catalog  
1. Launch Modelio.  
2. Navigate to **Configuration** > ![2] **Modules Catalog...**. 

![1]

### Step 2: Add the Module  
1. Click **Add a module to the catalog...**.  
2. Use the file browser to locate and select the downloaded `.jmdac` file.  
3. Click **OK** to add the module to the catalog.  

### Step 3: Install the Module in a Project



1. Open or create a project in Modelio.  
2. Expand the **Modules Catalog** by clicking the [![4]] icon.  
3. Select the **INTO-CPS module** from the catalog.  
4. Click the [![5]] icon to install the module in your project.

![3]

   [1]: img/module_catalog.png
   [2]: img/modulecatalog.png
   [3]: img/en-installingmodules.png
   [4]: img/maximize.png
   [5]: img/add.png


---

## Modelio Documentation  
For further guidance, refer to the following resources:  
- [Modelio User Manual](https://github.com/ModelioOpenSource/Modelio/wiki/Modelio-User-Documentation) *(wiki)*.  
- [Installing and Configuring Modules](https://github.com/ModelioOpenSource/Modelio/wiki/Modeler-_modeler_managing_projects_configuring_project_modules) *(wiki)*.  

---

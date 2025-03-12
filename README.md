# INTO-CPS SysML Profile for Modelio Open Source

The **INTO-CPS SysML Profile for Modelio** is an open-source module that extends the SysML language within the Modelio UML tool. This module provides specialized features for modeling and simulating cyber-physical systems (CPS) using the INTO-CPS (Integrated Toolchain for Model-based Design of Cyber-Physical Systems) framework. It is particularly useful for defining and configuring Functional Mock-up Units (FMUs) and generating simulation configurations for the Maestro co-simulation engine.

## Features

- **Architecture Diagram**: Model ports for FMI (Functional Mock-up Interface) input and output parameters. This helps define simulators and their interfaces.
- **Connections Diagram**: Create instance diagrams to model connections between FMI input and output parameters. Suitable for generating Maestro simulation configuration JSON files.
- **Design Space Exploration (DSE)**: Support for modeling parameters related to Design Space Exploration.
- **FMI Model Description Import/Export**: Import and export FMI model descriptions to facilitate interoperability with other tools.
- **Co-Simulation Configuration Export**: Export co-simulation JSON configurations for the [Maestro](https://into-cps-maestro.readthedocs.io/en/latest/user/getting-started.html) simulation engine.

## Documentation and Resources

- **Installation**: [install INTO-CPS module to Modelio](doc/Installation.md)
- **Quick Guide**: [INTO-CPS Modelio features](doc/QuickGuide.md)
- **Examples**: Examples are avalable in the [INTO-CPS application](https://into-cps-association.readthedocs.io/projects/desktop-application/en/latest/first_steps.html) in the SysML area for each example . Additional documentation will be migrated to this repository. Stay tuned for updates.

## TODO

> **Note**: Links in the documentation (e.g., forge.modelio.org) are outdated. All relevant information will be migrated to this repository.

1. **Module Migration**: Update the module to be compatible with the latest version of Modelio Open Source (5.4.00).
2. **Documentation Migration**: Migrate all documentation, examples, and guides to this repository for easier access and maintenance.
3. **Bug Fixes and Enhancements**: Address any known issues and add new features based on user feedback.

## Contributing

We welcome contributions from the community! If you'd like to contribute to the development of this module, please follow these steps:

1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Make your changes and ensure they are well-documented.
4. Submit a pull request with a detailed description of your changes.

## License

This project is licensed under the [INTO-CPS (GPL3) License](LICENSE). Feel free to use, modify, and distribute it as needed.

## Support

For questions, issues, or feature requests, please open an issue in this repository or contact the maintainers directly.

---

Thank you for using the **INTO-CPS SysML Profile for Modelio**! We hope this tool is helping you with your cyber-physical systems modeling and simulation workflows.

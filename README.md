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

1. **Module Migration**: done for Modelio Open Source 6.2 (module 1.8.00, see below).
2. **Documentation Migration**: Migrate all documentation, examples, and guides to this repository for easier access and maintenance.
3. **Bug Fixes and Enhancements**: Address any known issues and add new features based on user feedback.

## Modelio 6.2 (module 1.8.00)

INTOCPS 1.8.00 targets **Modelio Open Source 6.2** (binaryversion 6.2.00). Use 1.5.00 for Modelio 5.4.

- Built against the Modelio 6.2 API (MDAKit `[6.2.0,6.3.0)`), SysMLArchitect 6.2.00 and ModelerModule 9.6.1, with the official `modelio-maven-plugin` 6.2.0.00. No source change was needed.
- SysMLArchitect and ModelerModule are now `provided` and declared as required modules, instead of copies of old versions inside the module's `lib/` (sysmlarchitect 5.0.00, modelermodule 9.1.00 and 8.5.14).
- The module class path lists exactly the jars in `lib/` (stale entries removed: jgit 4.5, session, jsch, jaxb-api); the unused jackson 2.1.4/2.3.1 copies in `src/main/conf/lib` are removed.

Building: `repository.modelio.org` is offline. Install the Modelio 6.2 API and the official modules into your local Maven repository from a Modelio 6.2 installation (`sh tools/install-modelio-api.sh /path/to/modelio-6.2.0`), build `modelio-lib/modelio-maven-plugin` from the Modelio v6.2.0 source (`mvn install`), then `mvn clean package` → `target/INTOCPS_1.8.00.jmdac`.

Tested 2026-11-25 on Modelio Open Source 6.2.0 (macOS arm64, Java 21): module deployed with SysMLArchitect 6.2.00 and started; INTO-CPS block (EComponent) with Variable attributes created; FMI 2.0 modelDescription export with Real, Boolean and Integer variables; import back as Block with typed Variable attributes and StrtType interfaces.

Known issues seen in that test (behaviour of the module code, not related to Modelio 6.2):
- `initial` gets the start value (`initial="1.0"`); FMI 2.0 allows only `exact`, `approx` or `calculated`.
- every variable gets `valueReference="0"`; FMI requires unique value references.
- a Boolean variable without value is exported with `start="true"`.
- import does not restore the `Variable_variableKind` tag (parameter or variable).

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

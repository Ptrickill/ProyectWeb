package com.proyectoingweb.proyectoingweb.config;

import com.proyectoingweb.proyectoingweb.entity.*;
import com.proyectoingweb.proyectoingweb.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;
    
    @Autowired
    private CarreraService carreraService;
    
    @Autowired
    private MateriaService materiaService;
    
    @Autowired
    private HabilidadService habilidadService;
    
    @Autowired
    private PreguntaService preguntaService;
    
    @Autowired
    private CarreraMateriaService carreraMateriaService;
    
    @Autowired
    private CarreraHabilidadService carreraHabilidadService;
    
    @Autowired
    private CarreraAfinidadService carreraAfinidadService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n========================================");
        System.out.println("🚀 INICIALIZANDO DATOS DEL SISTEMA");
        System.out.println("========================================\n");
        
        // ==================== PASO 1: USUARIOS ====================
        initUsuarios();
        
        // ==================== PASO 2: MATERIAS ====================
        initMaterias();
        
        // ==================== PASO 3: HABILIDADES ====================
        initHabilidades();
        
        // ==================== PASO 4: CARRERAS ====================
        initCarreras();
        
        // ==================== PASO 5: PESOS DE CARRERAS ====================
        initPesos();
        
        System.out.println("\n========================================");
        System.out.println("✅ INICIALIZACIÓN COMPLETADA");
        System.out.println("========================================\n");
    }
    
    private void initUsuarios() {
        System.out.println("📋 Inicializando usuarios...");
        
        // Usuario administrador
        if (!userService.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin123"); 
            admin.setEmail("admin@ingenieriaweb.com");
            admin.setNombreCompleto("Administrador del Sistema");
            admin.setRole(User.Role.ADMIN);
            admin.setEnabled(true);
            
            userService.createUser(admin);
            System.out.println("   ✓ Usuario administrador creado: admin / admin123");
        }
        
        // Usuario de prueba
        if (!userService.existsByUsername("usuario")) {
            User user = new User();
            user.setUsername("usuario");
            user.setPassword("user123"); 
            user.setEmail("usuario@ingenieriaweb.com");
            user.setNombreCompleto("Usuario de Prueba");
            user.setRole(User.Role.USER);
            user.setEnabled(true);
            
            userService.createUser(user);
            System.out.println("   ✓ Usuario de prueba creado: usuario / user123");
        }
    }
    
    private void initMaterias() {
        System.out.println("\n📚 Inicializando materias...");
        
        String[][] materiasData = {
            {"Matemáticas", "Matemáticas"},
            {"Física", "Ciencias"},
            {"Química", "Ciencias"},
            {"Biología", "Ciencias"},
            {"Lengua y Literatura", "Lengua"},
            {"Historia", "Sociales"},
            {"Geografía", "Sociales"},
            {"Inglés", "Lengua"},
            {"Artes Visuales", "Arte"},
            {"Tecnología e Informática", "Tecnología"}
        };
        
        for (String[] materiaData : materiasData) {
            if (!materiaService.existsByNombre(materiaData[0])) {
                Materia materia = new Materia(materiaData[0], materiaData[1]);
                materiaService.createMateria(materia);
                System.out.println("   ✓ Materia creada: " + materiaData[0]);
            }
        }
    }
    
    private void initHabilidades() {
        System.out.println("\n🧠 Inicializando habilidades...");
        
        String[][] habilidadesData = {
            {"Razonamiento Lógico", "Capacidad para resolver problemas mediante el análisis y la lógica"},
            {"Creatividad", "Habilidad para generar ideas originales y soluciones innovadoras"},
            {"Empatía y Comunicación", "Capacidad para comprender a otros y comunicarse efectivamente"},
            {"Habilidad Manual y Espacial", "Destreza para trabajar con las manos y visualizar en 3D"},
            {"Liderazgo", "Capacidad para guiar, motivar y coordinar equipos de trabajo"}
        };
        
        for (String[] habilidadData : habilidadesData) {
            if (!habilidadService.existsByNombre(habilidadData[0])) {
                Habilidad habilidad = new Habilidad(habilidadData[0], habilidadData[1]);
                Habilidad saved = habilidadService.createHabilidad(habilidad);
                System.out.println("   ✓ Habilidad creada: " + habilidadData[0]);
                
                // Crear preguntas para esta habilidad
                initPreguntasHabilidad(saved);
            }
        }
    }
    
    private void initPreguntasHabilidad(Habilidad habilidad) {
        String nombreHabilidad = habilidad.getNombre();
        
        // Pregunta de GUSTO
        String textoGusto = "";
        String textoAutoevaluacion = "";
        
        switch (nombreHabilidad) {
            case "Razonamiento Lógico":
                textoGusto = "¿Te gusta resolver problemas matemáticos y acertijos?";
                textoAutoevaluacion = "¿Qué tan bueno eres para analizar situaciones y encontrar soluciones lógicas?";
                break;
            case "Creatividad":
                textoGusto = "¿Disfrutas diseñar, dibujar o crear cosas nuevas?";
                textoAutoevaluacion = "¿Qué tan fácil te resulta generar ideas originales?";
                break;
            case "Empatía y Comunicación":
                textoGusto = "¿Te gusta ayudar a las personas y escuchar sus problemas?";
                textoAutoevaluacion = "¿Qué tan bien te comunicas con los demás?";
                break;
            case "Habilidad Manual y Espacial":
                textoGusto = "¿Te gusta trabajar con tus manos y construir cosas?";
                textoAutoevaluacion = "¿Qué tan fácil te resulta visualizar objetos en 3D?";
                break;
            case "Liderazgo":
                textoGusto = "¿Te gusta tomar la iniciativa y dirigir grupos?";
                textoAutoevaluacion = "¿Qué tan bueno eres organizando y motivando equipos?";
                break;
        }
        
        // Crear pregunta de GUSTO
        Pregunta preguntaGusto = new Pregunta(textoGusto, "GUSTO", habilidad);
        preguntaService.createPregunta(preguntaGusto);
        
        // Crear pregunta de AUTOEVALUACION
        Pregunta preguntaAuto = new Pregunta(textoAutoevaluacion, "AUTOEVALUACION", habilidad);
        preguntaService.createPregunta(preguntaAuto);
    }
    
    private void initCarreras() {
        System.out.println("\n🎓 Inicializando carreras...");
        
        String[][] carrerasData = {
            {"ING-SIS", "Ingeniería de Sistemas", 
             "Diseña y desarrolla sistemas informáticos, software y soluciones tecnológicas para empresas y organizaciones."},
            
            {"MED", "Medicina", 
             "Estudia la salud humana, diagnostica y trata enfermedades para mejorar la calidad de vida de las personas."},
            
            {"DER", "Derecho", 
             "Estudia las leyes y normas jurídicas, defiende derechos y busca justicia en la sociedad."},
            
            {"ARQ", "Arquitectura", 
             "Diseña y planifica edificios y espacios urbanos combinando arte, funcionalidad y sostenibilidad."},
            
            {"PSI", "Psicología", 
             "Estudia el comportamiento humano y los procesos mentales para ayudar al bienestar emocional de las personas."}
        };
        
        for (String[] carreraData : carrerasData) {
            if (!carreraService.existsByCodigo(carreraData[0])) {
                Carrera carrera = new Carrera(carreraData[0], carreraData[1], carreraData[2]);
                carreraService.createCarrera(carrera);
                System.out.println("   ✓ Carrera creada: " + carreraData[1]);
            }
        }
    }
    
    private void initPesos() {
        System.out.println("\n⚖️  Inicializando pesos de carreras...");
        
        // INGENIERÍA DE SISTEMAS
        initPesosIngenieriaSistemas();
        
        // MEDICINA
        initPesosMedicina();
        
        // DERECHO
        initPesosDerecho();
        
        // ARQUITECTURA
        initPesosArquitectura();
        
        // PSICOLOGÍA
        initPesosPsicologia();
    }
    
    private void initPesosIngenieriaSistemas() {
        Carrera carrera = carreraService.getAllCarreras().stream()
            .filter(c -> c.getCodigo().equals("ING-SIS"))
            .findFirst().orElse(null);
        
        if (carrera == null) return;
        
        // Pesos de Materias
        asignarPesoMateria(carrera, "Matemáticas", 100f);
        asignarPesoMateria(carrera, "Física", 80f);
        asignarPesoMateria(carrera, "Tecnología e Informática", 100f);
        asignarPesoMateria(carrera, "Química", 40f);
        asignarPesoMateria(carrera, "Inglés", 60f);
        
        // Pesos de Habilidades
        asignarPesoHabilidad(carrera, "Razonamiento Lógico", 100f);
        asignarPesoHabilidad(carrera, "Creatividad", 70f);
        asignarPesoHabilidad(carrera, "Liderazgo", 60f);
        
        // Pesos de Afinidades
        asignarPesoAfinidad(carrera, "Matemáticas", 100f);
        asignarPesoAfinidad(carrera, "Tecnología", 100f);
        asignarPesoAfinidad(carrera, "Ciencias", 60f);
        
        System.out.println("   ✓ Pesos configurados para Ingeniería de Sistemas");
    }
    
    private void initPesosMedicina() {
        Carrera carrera = carreraService.getAllCarreras().stream()
            .filter(c -> c.getCodigo().equals("MED"))
            .findFirst().orElse(null);
        
        if (carrera == null) return;
        
        // Pesos de Materias
        asignarPesoMateria(carrera, "Biología", 100f);
        asignarPesoMateria(carrera, "Química", 100f);
        asignarPesoMateria(carrera, "Física", 60f);
        asignarPesoMateria(carrera, "Matemáticas", 50f);
        
        // Pesos de Habilidades
        asignarPesoHabilidad(carrera, "Empatía y Comunicación", 100f);
        asignarPesoHabilidad(carrera, "Razonamiento Lógico", 80f);
        asignarPesoHabilidad(carrera, "Habilidad Manual y Espacial", 70f);
        
        // Pesos de Afinidades
        asignarPesoAfinidad(carrera, "Ciencias", 100f);
        asignarPesoAfinidad(carrera, "Sociales", 60f);
        
        System.out.println("   ✓ Pesos configurados para Medicina");
    }
    
    private void initPesosDerecho() {
        Carrera carrera = carreraService.getAllCarreras().stream()
            .filter(c -> c.getCodigo().equals("DER"))
            .findFirst().orElse(null);
        
        if (carrera == null) return;
        
        // Pesos de Materias
        asignarPesoMateria(carrera, "Lengua y Literatura", 100f);
        asignarPesoMateria(carrera, "Historia", 90f);
        asignarPesoMateria(carrera, "Geografía", 60f);
        
        // Pesos de Habilidades
        asignarPesoHabilidad(carrera, "Empatía y Comunicación", 100f);
        asignarPesoHabilidad(carrera, "Razonamiento Lógico", 90f);
        asignarPesoHabilidad(carrera, "Liderazgo", 80f);
        
        // Pesos de Afinidades
        asignarPesoAfinidad(carrera, "Sociales", 100f);
        asignarPesoAfinidad(carrera, "Lengua", 80f);
        
        System.out.println("   ✓ Pesos configurados para Derecho");
    }
    
    private void initPesosArquitectura() {
        Carrera carrera = carreraService.getAllCarreras().stream()
            .filter(c -> c.getCodigo().equals("ARQ"))
            .findFirst().orElse(null);
        
        if (carrera == null) return;
        
        // Pesos de Materias
        asignarPesoMateria(carrera, "Matemáticas", 80f);
        asignarPesoMateria(carrera, "Física", 70f);
        asignarPesoMateria(carrera, "Artes Visuales", 100f);
        asignarPesoMateria(carrera, "Tecnología e Informática", 60f);
        
        // Pesos de Habilidades
        asignarPesoHabilidad(carrera, "Creatividad", 100f);
        asignarPesoHabilidad(carrera, "Habilidad Manual y Espacial", 100f);
        asignarPesoHabilidad(carrera, "Razonamiento Lógico", 70f);
        
        // Pesos de Afinidades
        asignarPesoAfinidad(carrera, "Arte", 100f);
        asignarPesoAfinidad(carrera, "Matemáticas", 70f);
        asignarPesoAfinidad(carrera, "Tecnología", 60f);
        
        System.out.println("   ✓ Pesos configurados para Arquitectura");
    }
    
    private void initPesosPsicologia() {
        Carrera carrera = carreraService.getAllCarreras().stream()
            .filter(c -> c.getCodigo().equals("PSI"))
            .findFirst().orElse(null);
        
        if (carrera == null) return;
        
        // Pesos de Materias
        asignarPesoMateria(carrera, "Biología", 70f);
        asignarPesoMateria(carrera, "Historia", 60f);
        asignarPesoMateria(carrera, "Lengua y Literatura", 80f);
        
        // Pesos de Habilidades
        asignarPesoHabilidad(carrera, "Empatía y Comunicación", 100f);
        asignarPesoHabilidad(carrera, "Razonamiento Lógico", 70f);
        asignarPesoHabilidad(carrera, "Liderazgo", 60f);
        
        // Pesos de Afinidades
        asignarPesoAfinidad(carrera, "Sociales", 100f);
        asignarPesoAfinidad(carrera, "Ciencias", 60f);
        
        System.out.println("   ✓ Pesos configurados para Psicología");
    }
    
    // Métodos auxiliares para asignar pesos
    private void asignarPesoMateria(Carrera carrera, String nombreMateria, Float peso) {
        Materia materia = materiaService.getAllMaterias().stream()
            .filter(m -> m.getNombre().equals(nombreMateria))
            .findFirst().orElse(null);
        
        if (materia != null) {
            try {
                CarreraMateria cm = new CarreraMateria(peso, carrera, materia);
                carreraMateriaService.createPeso(cm);
            } catch (Exception e) {
                // Ya existe, ignorar
            }
        }
    }
    
    private void asignarPesoHabilidad(Carrera carrera, String nombreHabilidad, Float peso) {
        Habilidad habilidad = habilidadService.getAllHabilidades().stream()
            .filter(h -> h.getNombre().equals(nombreHabilidad))
            .findFirst().orElse(null);
        
        if (habilidad != null) {
            try {
                CarreraHabilidad ch = new CarreraHabilidad(peso, carrera, habilidad);
                carreraHabilidadService.createPeso(ch);
            } catch (Exception e) {
                // Ya existe, ignorar
            }
        }
    }
    
    private void asignarPesoAfinidad(Carrera carrera, String area, Float peso) {
        try {
            CarreraAfinidad ca = new CarreraAfinidad(area, peso, carrera);
            carreraAfinidadService.createPeso(ca);
        } catch (Exception e) {
            // Ya existe, ignorar
        }
    }
}